package de.aswggmbh.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingListTest {
    @LocalServerPort int port;
    @Autowired ShoppingListItemRepository repository;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void emptyList() { repository.deleteAll(); }

    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String form) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getRendersEmptyListAndForm() throws Exception {
        var response = get("/");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Noch keine Artikel."));
        assertTrue(response.body().contains("name=\"name\""));
    }

    @Test
    void validPostPersistsAndRedirects() throws Exception {
        var response = post("name=Brot&demand=2&supply=0");
        assertEquals(302, response.statusCode());
        var root = URI.create("http://127.0.0.1:" + port + "/");
        assertEquals(root, root.resolve(response.headers().firstValue("Location").orElseThrow()));
        assertEquals(1, repository.count());
        assertEquals(2, repository.findByName("Brot").get(0).getDemand());
        assertTrue(get("/").body().contains("Brot"));
    }

    @Test
    void invalidPostShowsErrorsAndKeepsList() throws Exception {
        post("name=Brot&demand=2&supply=0");
        var response = post("name=&demand=-1&supply=-1");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Bitte einen Namen eingeben."));
        assertTrue(response.body().contains("Der Bedarf darf nicht negativ sein."));
        assertTrue(response.body().contains("Der Vorrat darf nicht negativ sein."));
        assertTrue(response.body().contains("Brot"));
        assertEquals(1, repository.count());
    }

    @Test
    void nonNumericQuantityIsAFieldError() throws Exception {
        var response = post("name=Milch&demand=abc&supply=0");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Bitte für den Bedarf eine ganze Zahl eingeben."));
        assertEquals(0, repository.count());
    }

    @Test
    void namesAreEscapedAndIdsCannotOverwriteItems() throws Exception {
        post("name=Brot&demand=2&supply=0");
        var id = repository.findByName("Brot").get(0).getId();
        post("id=" + id + "&name=%3Cscript%3Ealert(1)%3C%2Fscript%3E&demand=1&supply=0");
        assertEquals(2, repository.count());
        var html = get("/").body();
        assertFalse(html.contains("<script>"));
        assertTrue(html.contains("&lt;script&gt;"));
    }

    @Test
    void servletIsRegisteredAndReturnsJson() throws Exception {
        var response = get("/list");
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElseThrow().startsWith("application/json"));
        assertEquals("{\"items\":[]}", response.body().strip());
    }
}

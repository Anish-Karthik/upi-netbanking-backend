package site.anish_karthik.upi_net_banking.server.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class Router {
    private List<Route> getRoutes = new ArrayList<>();
    private List<Route> postRoutes = new ArrayList<>();
    private String pathPrefix;

    public Router(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    // Inner class to hold a route pattern and its handler
    @Data
    private static class Route {
        private Pattern pattern;
        private BiConsumer<HttpServletRequest, HttpServletResponse> handler;

        public Route(Pattern pattern, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
            this.pattern = pattern;
            this.handler = handler;
        }

    }

    // Method to handle GET requests with dynamic routes
    public void get(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        getRoutes.add(new Route(pattern, handler));
    }

    // Method to handle POST requests with dynamic routes
    public void post(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        postRoutes.add(new Route(pattern, handler));
    }

    // Convert the Express-style route to a regex, e.g. "/user/:userId" to "/user/([^/]+)"
    private String convertToRegex(String path) {
        return "^" + pathPrefix + path.replaceAll(":([a-zA-Z][a-zA-Z0-9_]*)", "([^/]+)") + "$";
    }

    // Handle incoming request by matching the method and route
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        System.out.println(path);
        List<Route> routes = "GET".equalsIgnoreCase(method) ? getRoutes : postRoutes;
        System.out.println(routes.get(0).pattern);
        for (Route route : routes) {
            Matcher matcher = route.getPattern().matcher(path);
            if (matcher.matches()) {
                // Store route parameters
                Map<String, String> params = extractParams(route.getPattern(), path);

                // Add parameters to request attributes (so they can be accessed later)
                req.setAttribute("routeParams", params);

                // Execute the handler
                route.getHandler().accept(req, resp);
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Route not found");
    }

    // Extract route parameters from a matched URL
    private Map<String, String> extractParams(Pattern pattern, String path) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = pattern.matcher(path);

        if (matcher.matches()) {
            // Extract named capturing groups
            Pattern paramPattern = Pattern.compile(":([a-zA-Z][a-zA-Z0-9_]*)");
            Matcher paramMatcher = paramPattern.matcher(pattern.pattern());

            int i = 1;
            while (paramMatcher.find()) {
                params.put(paramMatcher.group(1), matcher.group(i++));
            }
        }
        return params;
    }
}

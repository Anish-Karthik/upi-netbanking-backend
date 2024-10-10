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

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Router {
    private List<Route> getRoutes = new ArrayList<>();
    private List<Route> postRoutes = new ArrayList<>();
    private List<Route> putRoutes = new ArrayList<>();
    private List<Route> patchRoutes = new ArrayList<>();
    private List<Route> deleteRoutes = new ArrayList<>();
    private String pathPrefix;

    public Router(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @Data
    private static class Route {
        private Pattern pattern;
        private BiConsumer<HttpServletRequest, HttpServletResponse>     handler;

        public Route(Pattern pattern, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
            this.pattern = pattern;
            this.handler = handler;
        }
    }

    public void get(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        getRoutes.add(new Route(pattern, handler));
    }

    public void post(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        postRoutes.add(new Route(pattern, handler));
    }

    public void put(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        putRoutes.add(new Route(pattern, handler));
    }

    public void patch(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        patchRoutes.add(new Route(pattern, handler));
    }

    public void delete(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        deleteRoutes.add(new Route(pattern, handler));
    }

    private String convertToRegex(String path) {
        return "^" + pathPrefix + path.replaceAll(":([a-zA-Z][a-zA-Z0-9_]*)", "([^/]+)") + "$";
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        List<Route> routes;

        switch (method.toUpperCase()) {
            case "GET":
                routes = getRoutes;
                break;
            case "POST":
                routes = postRoutes;
                break;
            case "PUT":
                routes = putRoutes;
                break;
            case "PATCH":
                routes = patchRoutes;
                break;
            case "DELETE":
                routes = deleteRoutes;
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed");
                return;
        }

        for (Route route : routes) {
            Matcher matcher = route.getPattern().matcher(path);
            if (matcher.matches()) {
                Map<String, String> params = extractParams(route.getPattern(), path);
                req.setAttribute("routeParams", params);
                route.getHandler().accept(req, resp);
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Route not found");
    }

    private Map<String, String> extractParams(Pattern pattern, String path) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = pattern.matcher(path);

        if (matcher.matches()) {
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
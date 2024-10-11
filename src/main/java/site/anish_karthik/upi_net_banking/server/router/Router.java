package site.anish_karthik.upi_net_banking.server.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class Router {
    private Map<String, Map<Pattern, BiConsumer<HttpServletRequest, HttpServletResponse>>> routes = new HashMap<>();
    private String pathPrefix;

    public Router(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public void get(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        addRoute("GET", path, handler);
    }

    public void post(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        addRoute("POST", path, handler);
    }

    public void put(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        addRoute("PUT", path, handler);
    }

    public void patch(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        addRoute("PATCH", path, handler);
    }

    public void delete(String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        addRoute("DELETE", path, handler);
    }

    private void addRoute(String method, String path, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        Pattern pattern = Pattern.compile(convertToRegex(path));
        routes.computeIfAbsent(method, k -> new HashMap<>()).put(pattern, handler);
    }

    private String convertToRegex(String path) {
        return "^" + pathPrefix + path.replaceAll(":([a-zA-Z][a-zA-Z0-9_]*)", "([^/]+)") + "$";
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod().toUpperCase();
        String path = req.getRequestURI();
        Map<Pattern, BiConsumer<HttpServletRequest, HttpServletResponse>> methodRoutes = routes.get(method);

        if (methodRoutes == null) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed");
            return;
        }

        for (Map.Entry<Pattern, BiConsumer<HttpServletRequest, HttpServletResponse>> entry : methodRoutes.entrySet()) {
            Matcher matcher = entry.getKey().matcher(path);
            if (matcher.matches()) {
                Map<String, String> params = extractParams(entry.getKey(), path);
                req.setAttribute("routeParams", params);
                entry.getValue().accept(req, resp);
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
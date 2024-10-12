package site.anish_karthik.upi_net_banking.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CustomResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream output;
    private PrintWriter writer;

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
        writer = new PrintWriter(output);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public String getCapturedResponse() throws IOException {
        writer.flush(); // Ensure all data is written to the output stream
        return output.toString("UTF-8");
    }

    public void setModifiedResponse(String json) throws IOException {
        HttpServletResponse response = (HttpServletResponse) getResponse();
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}

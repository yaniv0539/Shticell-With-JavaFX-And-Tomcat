package servlets.sheets.cells;

import com.google.gson.Gson;
import jakarta.servlet.annotation.MultipartConfig;
import dto.CellDto;
import dto.SheetDto;
import engine.api.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "CellServlet", urlPatterns = "/sheet/cell")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CellServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Engine engine = ServletUtils.getEngine(getServletContext());
            Gson gson = ServletUtils.getGson(getServletContext());

            String userName = ServletUtils.getUserName(request);
            String sheetName = ServletUtils.getSheetName(request);

            SheetDto sheetDTO = engine.getSheetDTO(userName, sheetName);

            if (sheetDTO == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ServletException("Invalid sheet name");
            }

            String cellName = ServletUtils.getCellName(request);
            CellDto cellDto = sheetDTO.activeCells().get(cellName);

            if (cellDto == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ServletException("Invalid cell name");
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(gson.toJson(cellDto));
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Engine engine = ServletUtils.getEngine(getServletContext());
            Gson gson = ServletUtils.getGson(getServletContext());

            String userName = ServletUtils.getUserName(request);
            String sheetName = ServletUtils.getSheetName(request);
            String cellName = ServletUtils.getCellName(request);

            int sheetVersion = ServletUtils.getSheetVersion(request);

            String jsonBody = ServletUtils.getJsonBody(request);

            synchronized (ServletUtils.getVersionLock()){
                engine.updateCell(userName, sheetName, sheetVersion, cellName, jsonBody);
            }

            SheetDto sheetDTO = engine.getSheetDTO(userName, sheetName);

            response.getWriter().print(gson.toJson(sheetDTO));
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class DataController {
    @GetMapping(value = "/getDataAsCsv", produces = "text/csv")
    public ResponseEntity getDataAsCsv(@RequestParam("fileName") String fileName) {
        //List<DataPoint> dataPoints = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new File(ResourceUtils.getFile(fileName).getAbsolutePath()));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);

            String header = "BATTER,LAUNCH_ANGLE,EXIT_SPEED";
            printWriter.println(header);
            rowIterator.next();  
            int count = 0;  
            while (rowIterator.hasNext() && count < 26) {
                Row row = rowIterator.next();
                String BATTER = row.getCell(1).getStringCellValue();
                double x = row.getCell(5).getNumericCellValue();
                double y = row.getCell(6).getNumericCellValue();

                printWriter.println("\"" + BATTER +"\"" + "," + x + "," + y); // "\"" + BATTER +"\"" helps with comma in excel sheet
                
                count++;
            }
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=csvData.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(outputStream.toByteArray());
        }
        catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return null;
    }
}

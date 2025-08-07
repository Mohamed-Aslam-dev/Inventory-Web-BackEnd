package com.inventory_management;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "https://manageinventoryapp.netlify.app/")
public class InventoryController {
	
	@Autowired
    private InventoryService inventoryService;

	@GetMapping("/home")
    public void homePage(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://manageinventoryapp.netlify.app/");
    }
	

    @PostMapping("/create-csv")
    public ResponseEntity<String> createCSVFile(@RequestBody Map<String,String> data) throws IOException {
    	String fileName = data.get("companyName");
        String message = inventoryService.createCsvFile(fileName);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/list-csv-files")
    public ResponseEntity<List<String>> viewListOfCsvFiles(){
    	
    	List<String> fileNames = inventoryService.viewListOfCsvFile();
    	
    	return ResponseEntity.ok(fileNames);
    }
	
    @PostMapping("/add-row")
    public ResponseEntity<String> addRow(@RequestBody Map<String, String> data) throws IOException {
        String fileName = data.get("companyName");
        String productName = data.get("productName");
        String msg = inventoryService.addRowToCsv(fileName, productName);
        return ResponseEntity.ok(msg);
    }
    
    @GetMapping("/products/{fileName}")
    public ResponseEntity<List<String>> getProductsFromCsv(@PathVariable String fileName) {
//        String csvPath = "path/to/csv/folder/" + fileName + ".csv"; // or .csv already included
        List<String> products = inventoryService.readProductNamesFromCSV(fileName);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PDFRequestDTO request) {

        List<ProductsDTO> products = request.getProducts();

        boolean hasValidQuantity = products.stream()
                .anyMatch(p -> p.getProductQuantity() >= 1);

        if (!hasValidQuantity) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] pdfBytes = inventoryService.createPDF(products, request.getCompanyName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+request.getCompanyName()+".pdf")
                .body(pdfBytes);
    }

}

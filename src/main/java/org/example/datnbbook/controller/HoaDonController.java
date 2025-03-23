package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/hoa-don")
    public ResponseEntity<List<HoaDon>> getHoaDon() {
        List<HoaDon> hoaDons = hoaDonService.getAllHoaDon();
        if (hoaDons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hoaDons, HttpStatus.OK);
    }

    @GetMapping("/hoa-don/{id}")
    public ResponseEntity<HoaDon> getHoaDon(@PathVariable int id) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hoaDon, HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) {
        try {
            ByteArrayOutputStream pdfOutput = hoaDonService.printInvoiceToPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don_" + id + ".pdf");
            return new ResponseEntity<>(pdfOutput.toByteArray(), headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException | com.google.zxing.WriterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hoa-don/export-excel")
    public ResponseEntity<byte[]> exportHoaDonToExcel() {
        try {
            ByteArrayOutputStream excelOutput = hoaDonService.exportHoaDonToExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "danh_sach_hoa_don.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(excelOutput.toByteArray(), headers, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.service.GoogleService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/google-drive")
class GoogleDriveController(
    private val googleService: GoogleService
) {

    @PostMapping("/upload")
    fun handleFileUpload(@RequestParam ("file") file: MultipartFile): ResponseEntity<Any> {
        if (file.isEmpty) {
            throw LivrariaException(HttpStatus.BAD_REQUEST, "Arquivo vazio")
        }
        val tempFile: File = File.createTempFile("file", file.originalFilename)
        file.transferTo(tempFile)
        googleService.uploadFile(tempFile)
        return ResponseEntity.ok(file.originalFilename)
    }

    @GetMapping("/listarArquivos")
    fun listarArquivos() = googleService.listFiles()

    @GetMapping("/download/{fileId}")
    fun downloadFile(@PathVariable fileId: String, response: HttpServletResponse) {
        val bookFile = googleService.getBookFileById(fileId)
        val tempFile = File(System.getProperty("java.io.tmpdir") + "/${bookFile!!.fileName}")
        googleService.downloadFile(bookFile.googleDriveFileId, tempFile.path)
        response.contentType = bookFile.contentType
        response.setHeader("Content-Disposition", "attachment; filename=\"${bookFile.fileName}\"")
        tempFile.inputStream().copyTo(response.outputStream)
    }

}
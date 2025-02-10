package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.repository.LivroRepository
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
    private val googleService: GoogleService,
    private val livroRepository: LivroRepository
) {

    @PostMapping("/upload/{idLivro}")
    fun handleFileUpload(@RequestParam ("file") file: MultipartFile, @PathVariable idLivro: Long): ResponseEntity<Any> {
        if (file.isEmpty) {
            throw LivrariaException(HttpStatus.BAD_REQUEST, "Arquivo vazio")
        }
        val livro = livroRepository.findById(idLivro).orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "Livro não encontrado") }
        val tempFile: File = File.createTempFile("file", file.originalFilename)
        file.transferTo(tempFile)
        val arquivoEnviado = googleService.uploadFile(tempFile, livro)
        return ResponseEntity.ok("Arquivo enviado com sucesso!! Detalhes: $arquivoEnviado")
    }

    @GetMapping("/listarArquivos")
    fun listarArquivos() = googleService.listFiles()

    @GetMapping("/obterFileId/{livroId}")
    fun obterFileId(@PathVariable livroId: Long): ResponseEntity<Map<String, String>> {
        val livro = livroRepository.findById(livroId)
            .orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "Livro não encontrado") }

        val arquivoLivro = livro.arquivoLivro
            ?: throw LivrariaException(HttpStatus.NOT_FOUND, "Nenhum arquivo associado a este livro")

        return ResponseEntity.ok(mapOf("fileId" to arquivoLivro.googleDriveFileId))
    }


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
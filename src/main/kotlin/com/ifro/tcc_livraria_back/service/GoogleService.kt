package com.ifro.tcc_livraria_back.service

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.ArquivoLivro
import com.ifro.tcc_livraria_back.model.Livro
import com.ifro.tcc_livraria_back.repository.ArquivoLivroRepository
import com.ifro.tcc_livraria_back.repository.LivroRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections

@Service
class GoogleService(
    private val arquivoLivroRepository: ArquivoLivroRepository,
    private val livroRepository: LivroRepository
) {

    val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
    val serviceAccountKeyPath: String = getPathToGoogleCredentials()

    private fun getPathToGoogleCredentials(): String {
        val currentDirectory: String = System.getProperty("user.dir")
        val filePath: Path = Paths.get(currentDirectory, "google_credentials.json")

        return filePath.toString()
    }

    fun uploadFile(file: File, livro: Livro): String {
        try {
            val folderId = "1Rreh_OgeZlZRWhl6550g3WPSU5pHJNAU"
            val drive: Drive = createDriveService()

            val fileMetaData: com.google.api.services.drive.model.File = com.google.api.services.drive.model.File()
            fileMetaData.setName(file.name)
            fileMetaData.parents = Collections.singletonList(folderId)
            val mediaContent = FileContent("application/pdf", file)
            val uploadedFile: com.google.api.services.drive.model.File = drive.files().create(fileMetaData, mediaContent)
                .setFields("id")
                .execute()
            val fileUrl = "https://drive.google.com/file/d/${uploadedFile.id}/view?usp=sharing"
            val bookFile = ArquivoLivro(
                googleDriveFileId = uploadedFile.id,
                fileName = file.name,
                contentType = "application/pdf",
                livro = livro // Associa o ArquivoLivro ao Livro
            )
            arquivoLivroRepository.save(bookFile)

            // Atualiza o Livro com a referência ao ArquivoLivro e salva o Livro
            livro.arquivoLivro = bookFile
            livroRepository.save(livro)

            if (file.exists()) file.delete()
            return "${fileUrl}, ${uploadedFile.id}"
        } catch (e: Exception) {
            throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)
        }
    }


    fun downloadFile(fileId: String, destinationPath: String) {
        try {
            val drive: Drive = createDriveService()

            val outputStream: OutputStream = FileOutputStream(destinationPath)
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            outputStream.close()
        } catch (e: Exception) {
            throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao fazer download do arquivo: ${e.message}")
        }
    }

    fun getBookFileById(id: String): ArquivoLivro? {
        return arquivoLivroRepository.findByGoogleDriveFileId(id)
    }

    fun listFiles(): List<ArquivoLivro> {
        return arquivoLivroRepository.findAll()
    }

    private fun createDriveService(): Drive {
        val credential: GoogleCredential = GoogleCredential.fromStream(FileInputStream(serviceAccountKeyPath))
            .createScoped(Collections.singletonList(DriveScopes.DRIVE))

        return Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            jsonFactory,
            credential)
            .build()
    }

}
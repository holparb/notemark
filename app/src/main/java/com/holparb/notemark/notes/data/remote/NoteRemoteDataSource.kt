package com.holparb.notemark.notes.data.remote

import com.holparb.notemark.core.data.networking.NOTES_ENDPOINT
import com.holparb.notemark.core.data.networking.constructUrl
import com.holparb.notemark.core.data.networking.safeCall
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.result.map
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class NoteRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getNotes(
        page: Int = -1,
        size: Int = 20
    ): Result<List<NoteDto>, NetworkError> {
        return safeCall<NotesResponseDto> {
            client.get(urlString = constructUrl(NOTES_ENDPOINT)) {
                parameter("page", page)
                parameter("size", size)
            }
        }.map { it.notes }
    }

    suspend fun createNote(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeCall<NoteDto> {
            client.post(urlString = constructUrl(NOTES_ENDPOINT)) {
                setBody(note)
            }
        }
    }

    suspend fun updateNote(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeCall<NoteDto> {
            client.put(urlString = constructUrl(NOTES_ENDPOINT)) {
                setBody(note)
            }
        }
    }

    suspend fun deleteNote(noteId: String): Result<Unit, NetworkError> {
        return safeCall<Unit> {
            client.delete(urlString = constructUrl("$NOTES_ENDPOINT/$noteId"))
        }
    }
}
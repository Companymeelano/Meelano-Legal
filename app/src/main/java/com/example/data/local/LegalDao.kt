package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EblaghItem
import com.example.data.model.JudicialDeadline
import com.example.data.model.LegalCase
import com.example.data.model.LegalDraft
import kotlinx.coroutines.flow.Flow

@Dao
interface LegalCaseDao {
    @Query("SELECT * FROM legal_cases ORDER BY id DESC")
    fun getAllCases(): Flow<List<LegalCase>>

    @Query("SELECT * FROM legal_cases WHERE id = :id")
    fun getCaseById(id: Long): Flow<LegalCase?>

    @Query("SELECT * FROM legal_cases WHERE caseTitle LIKE '%' || :query || '%' OR caseNumber LIKE '%' || :query || '%' OR clientName LIKE '%' || :query || '%'")
    fun searchCases(query: String): Flow<List<LegalCase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(legalCase: LegalCase): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCases(cases: List<LegalCase>)

    @Update
    suspend fun updateCase(legalCase: LegalCase)

    @Query("DELETE FROM legal_cases WHERE id = :id")
    suspend fun deleteCaseById(id: Long)

    @Query("SELECT COUNT(*) FROM legal_cases")
    suspend fun getCasesCount(): Int
}

@Dao
interface JudicialDeadlineDao {
    @Query("SELECT * FROM judicial_deadlines ORDER BY daysRemaining ASC")
    fun getAllDeadlines(): Flow<List<JudicialDeadline>>

    @Query("SELECT * FROM judicial_deadlines WHERE isCompleted = 0 ORDER BY daysRemaining ASC")
    fun getActiveDeadlines(): Flow<List<JudicialDeadline>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadline(deadline: JudicialDeadline): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadlines(deadlines: List<JudicialDeadline>)

    @Update
    suspend fun updateDeadline(deadline: JudicialDeadline)

    @Query("DELETE FROM judicial_deadlines WHERE id = :id")
    suspend fun deleteDeadlineById(id: Long)
}

@Dao
interface EblaghDao {
    @Query("SELECT * FROM eblagh_items ORDER BY id DESC")
    fun getAllEblagh(): Flow<List<EblaghItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEblagh(item: EblaghItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEblagh(items: List<EblaghItem>)

    @Update
    suspend fun updateEblagh(item: EblaghItem)

    @Query("DELETE FROM eblagh_items WHERE id = :id")
    suspend fun deleteEblaghById(id: Long)
}

@Dao
interface LegalDraftDao {
    @Query("SELECT * FROM legal_drafts ORDER BY id DESC")
    fun getAllDrafts(): Flow<List<LegalDraft>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: LegalDraft): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrafts(drafts: List<LegalDraft>)

    @Query("DELETE FROM legal_drafts WHERE id = :id")
    suspend fun deleteDraftById(id: Long)
}

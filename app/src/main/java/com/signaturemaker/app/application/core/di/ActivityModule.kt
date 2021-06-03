package com.signaturemaker.app.application.core.di

import android.content.Context
import com.signaturemaker.app.data.repositories.FilesRepositoryImp
import com.signaturemaker.app.data.repositories.SignatureRepositoryImp
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.GetAllFiles
import com.signaturemaker.app.domain.usecase.MoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveFile
import com.signaturemaker.app.domain.usecase.SaveBitmap
import com.signaturemaker.app.domain.usecase.SendSuggest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ActivityModule {

    @Provides
    fun provideSendSuggest(dataSource: SignatureRepositoryImp): SendSuggest = SendSuggest(dataSource)

    @Provides
    fun provideSaveBitmap(dataSource: FilesRepositoryImp): SaveBitmap = SaveBitmap(dataSource)

    @Provides
    fun provideMoveFiles(dataSource: FilesRepositoryImp): MoveAllFiles = MoveAllFiles(dataSource)

    @Provides
    fun provideRemoveAllFiles(
        removeFile: RemoveFile,
        getAllFiles: GetAllFiles
    ): RemoveAllFiles =
        RemoveAllFiles(removeFile, getAllFiles)

    @Provides
    fun provideGetAllFiles(dataSource: FilesRepositoryImp): GetAllFiles = GetAllFiles(dataSource)

    @Provides
    fun provideGetRemoveFile(dataSource: FilesRepositoryImp): RemoveFile = RemoveFile(dataSource)

    @Provides
    fun provideFilesRepository(@ApplicationContext appContext: Context): FilesRepository = FilesRepositoryImp(appContext)

}

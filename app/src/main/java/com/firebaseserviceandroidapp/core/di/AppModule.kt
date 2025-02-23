package com.firebaseserviceandroidapp.core.di

import android.app.Application
import android.content.Context
import com.example.reviewapisandroid.features.home.data.repository.MainLayoutRepository
import com.firebaseserviceandroidapp.config.preferences.PreferenceHelper
import com.firebaseserviceandroidapp.features.add_note_dialog.data.repository.AddNoteDialogRepository
import com.firebaseserviceandroidapp.features.home.data.repository.HomeRepository
import com.firebaseserviceandroidapp.features.login.data.repository.LoginRepository
import com.firebaseserviceandroidapp.features.profile.data.repository.ProfileRepository
import com.firebaseserviceandroidapp.features.register.data.repository.RegisterRepository
import com.firebaseserviceandroidapp.features.search.data.repository.SearchRepository
import com.firebaseserviceandroidapp.features.setting.data.repository.SettingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMainLayoutRepository(
        @ApplicationContext context: Context,
        firestore: FirebaseFirestore,
    ): MainLayoutRepository {
        return MainLayoutRepository(context, firestore)
    }

    @Provides
    fun provideLoginRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore,
    ): LoginRepository {
        return LoginRepository(
            firebaseAuth, firebaseFireStore
        )
    }

    @Provides
    fun provideRegisterRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore,
    ): RegisterRepository {
        return RegisterRepository(
            firebaseAuth, firebaseFireStore
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun provideAddNoteDialogRepository(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): AddNoteDialogRepository {
        return AddNoteDialogRepository(firebaseFireStore, firebaseAuth)
    }

    @Provides
    fun provideHomeRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): HomeRepository {
        return HomeRepository(
            firestore, firebaseAuth
        )
    }

    @Provides
    fun provideProfileRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore,
        @ApplicationContext context: Context,
    ): ProfileRepository {
        return ProfileRepository(
            firebaseAuth, firebaseFireStore , context
        )
    }

    @Provides
    fun provideSearchRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): SearchRepository {
        return SearchRepository(
            fireStore, firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun providePreferenceHelper(@ApplicationContext context: Context): PreferenceHelper {
        return PreferenceHelper(context)
    }

    @Provides
    fun provideSettingRepository(
        preferenceHelper: PreferenceHelper,
    ): SettingRepository {
        return SettingRepository(
             preferenceHelper
        )
    }
}
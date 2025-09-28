package com.example.phonecontact.presentation.addcontact

sealed class NewContactEvent {
    data class FirstNameChanged(val firstName: String) : NewContactEvent()
    data class LastNameChanged(val lastName: String) : NewContactEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : NewContactEvent()
    data class ProfileImageSelected(val uri: String, val imageBytes: ByteArray? = null) : NewContactEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ProfileImageSelected
            if (uri != other.uri) return false
            if (imageBytes != null) {
                if (other.imageBytes == null) return false
                if (!imageBytes.contentEquals(other.imageBytes)) return false
            } else if (other.imageBytes != null) return false
            return true
        }

        override fun hashCode(): Int {
            var result = uri.hashCode()
            result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
            return result
        }
    }
    data object SaveContact : NewContactEvent()
    data object CancelClicked : NewContactEvent()
    data object AddPhotoClicked : NewContactEvent()
    data object DismissPhotoSheet : NewContactEvent()
    data object CameraSelected : NewContactEvent()
    data object GallerySelected : NewContactEvent()
    data object ContactSavedAcknowledged : NewContactEvent()
}
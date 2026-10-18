package cat.freya.lodestone.publishing

class PublishArguments {
    val name: String? = null
    var artifact: String? = null
    var group: String? = null
    var author: String? = null
    var license: License? = null
    var gitRepo: String? = null
    var website: String? = null
    var sign: Boolean = false
    internal val repositories = mutableListOf<MavenRepository>()

    fun mavenCentral() {
        repositories.add(MavenRepository.MAVEN_CENTERAL)
    }

    fun maven(url: String) {
        maven {
            this.url = url
        }
    }

    fun reposilite(url: String) {
        maven {
            this.url = url
            this.redirect = true
        }
    }

    fun maven(fn: MavenRepository.() -> Unit) {
        val repo = MavenRepository()
        fn(repo)
        repositories.add(repo)
    }
}

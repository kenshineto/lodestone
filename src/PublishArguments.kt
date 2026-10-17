package cat.freya.lodestone

class PublishArguments {
    // pom metadata
    var group = ""
    var author = ""
    var license = License.GPL3
    var artifact = ""
    var url = ""
    // repo
    var repo = "https://maven.freya.cat"
    var sign = true
    // authentication
    var username = ""
    var password = ""
    var auth = "basic"
}

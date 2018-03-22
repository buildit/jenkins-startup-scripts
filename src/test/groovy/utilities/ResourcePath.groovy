package utilities

class ResourcePath {
    static def String resourcePath(String path, String basePath="") {
        String fullPath = "${basePath}/${path}"
        if(basePath.length()==0){
            fullPath = "${path}"
        }
        def result = URLDecoder.decode(new ResourcePath().getClass().getClassLoader().getResource(fullPath).toString().replace("file:", ""), "UTF8")
        println("Full path of ${path} is ${result}")
        return result == null || result.endsWith("null") ? null : result
    }
}

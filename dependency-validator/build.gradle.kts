plugins {
    application
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.ibm.wala:com.ibm.wala.core:1.5.4")
    implementation("com.ibm.wala:com.ibm.wala.shrike:1.5.4")
    implementation("com.ibm.wala:com.ibm.wala.util:1.5.4")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("org.apache.commons:commons-csv:1.10.0")
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-analysis:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
}



application {
    mainClass.set("com.uber.dependency.validator.App")
}

package me.paulschwarz.springdotenv;

public record DotenvConfig(
    boolean enabled,
    String directory,
    String filename,
    boolean ignoreIfMalformed,
    boolean ignoreIfMissing,
    boolean exportToSystemProperties
) {

    public static DotenvConfig defaults() {
        return new DotenvConfig(
            true,
            null,
            ".env",
            false,
            true,
            false);
    }
}

# Changelog

## [Unreleased]

### 🚨 Breaking Changes

#### Configuration renames

- **`springdotenv.systemProperties` → `springdotenv.exportToSystemProperties`**  
  The configuration key (and corresponding environment variable
  `SPRINGDOTENV_SYSTEM_PROPERTIES`) has been replaced by
  `springdotenv.exportToSystemProperties` /
  `SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES`.

  Update application properties, environment variables, and any scripts or
  documentation that control exporting dotenv values into system properties.

  `systemProperties` is deprecated but still supported with warning.

#### Artifact split & coordinates

The project is now published as multiple artifacts. Consumers must choose the
appropriate module:

- `me.paulschwarz:spring-dotenv`  
  Core library for Spring Framework (no Spring Boot integration)

- `me.paulschwarz:springboot3-dotenv`

- `me.paulschwarz:springboot4-dotenv`

Older single-artifact setups must migrate to the correct module.  
**Spring Boot 2 integration is no longer provided.**

#### Version alignment via BOM

- Introduced **`me.paulschwarz:spring-dotenv-bom`** to manage versions across all
  modules.
- Consumers should import the BOM (`platform(...)` in Gradle,
  `<dependencyManagement>` in Maven).
- Pinning individual module versions directly is now discouraged.

#### Java baseline

- The Java toolchain now targets **Java 17**.
- Java 8 and Java 11 are no longer supported.
- This aligns with Spring Boot 3+, which itself requires Java 17.

#### Prefix handling

- Prefix-based access is no longer supported.
- The library now defaults to **no prefix**.
- Applications should migrate to unprefixed property access.

---

### ✨ New & Notable Additions

#### Spring integration kill switch

- New configuration option: **`springdotenv.enabled`**
  (or `SPRINGDOTENV_ENABLED`)
- Defaults to `true`
- Set to `false` to bypass loading `.env` values into the Spring `Environment`

This allows explicit opt-out when using the core library or custom integration.

#### Spring Boot 3 and 4 support

- Added dedicated **`springboot3-dotenv`** and **`springboot4-dotenv`** modules.
- Includes a Boot 3 and a Boot 4–compatible environment post-processor respectively.

---

### 🧱 Build, Tooling & Infrastructure

- Gradle upgraded to 9.2.1.
- Java toolchain configuration centralized in build logic.
- Error Prone checks reintroduced.
- CI/CD workflows modernized and consolidated.
    - Clear separation between CI, snapshot publishing, and tagged releases.
    - Default branch renamed to `main`.
    - Improved snapshot handling.

---

### 📦 Installation

**Recommended approach**

1. Import `me.paulschwarz:spring-dotenv-bom`
2. Depend on exactly one runtime module:
    - `spring-dotenv`, or
    - `springboot3-dotenv`, or
    - `springboot4-dotenv`

See `docs/release-install.md` for Maven and Gradle examples.

---

### 🧪 Testing

- Not run for this snapshot
- QA review only

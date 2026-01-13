# Copilot instructions — jshERP

## Quick context ✅
- Full-stack ERP: **Backend** = Spring Boot (JDK8) + MyBatis / MyBatis-Generator + MyBatis‑Plus + Starblues plugin framework; **Frontend** = Vue 2 + Ant Design Vue. See `README.md` and `jshERP-web/README.md`.
- Main backend entry: `com.jsh.erp.ErpApplication` (`jshERP-boot` module). Swagger/docs available at `/jshERP-boot/doc.html` when server runs.

## Developer actions (explicit commands) 🔧
- Backend (dev):
  - Run in IDE or: `cd jshERP-boot && mvn spring-boot:run`
  - Package for distribution: `mvn -Dmaven.compiler.skip=false clean package -DskipTests` (note: project sets the compiler plugin to skip by default)
  - After package you can run the jar: `java -jar jshERP-boot/target/jshERP.jar` or use the assembled distribution and `bin/run-manage.sh start`.
- Frontend (dev & build):
  - `cd jshERP-web && yarn install && yarn serve` (dev)
  - `cd jshERP-web && yarn build` (production build)
- MyBatis code generation: the config is `jshERP-boot/src/test/resources/generatorConfig.xml`. Run generator via module: `mvn -pl jshERP-boot mybatis-generator:generate` (update local classpath and DB credentials first).

## Key code/layout pointers 📁
- Backend module: `jshERP-boot`
  - Entities: `com.jsh.erp.datasource.entities`
  - Mappers: `com.jsh.erp.datasource.mappers`
  - Mapper XML: `src/main/resources/mapper_xml/` (conventional MyBatis XML files)
  - SQL / schema seeds: `DB_design/ddl.sql`, `jshERP-boot/docs/jsh_erp.sql`
- Frontend module: `jshERP-web`
  - API wrapper: `src/utils/request.js` (base URL default `/jshERP-boot`), `src/api/*.js` contains concrete endpoints
  - Plugin UI: `src/views/system/PluginList.vue`, `src/views/system/SystemConfigList.vue`

## Project conventions & patterns 🔍
- Database conventions:
  - Columns are snake_case in DB and map to camelCase fields in Java entities.
  - Soft delete flag: `delete_flag` is used across mappers.
  - `tenant_id` appears in many tables (multi-tenant aware patterns).
- Plugin architecture:
  - Backend uses the **starblues** plugin framework (plugins managed at runtime).
  - Frontend talks to endpoints such as `/plugin/list`, `/plugin/start/{id}`, `/plugin/stop/{id}`, `/plugin/uploadInstallPluginJar`.
- Tokens: frontend sends `X-Access-Token` header from localStorage (see `request.js`).
- Generated code: MyBatis generator writes Java entities to `src/main/java` and XML to `src/main/resources/mapper_xml`; keep generator config updated to match local env.

## Important gotchas & security notes ⚠️
- Sensitive data found in repo: `application.yml` contains inline Aliyun credentials; `generatorConfig.xml` contains DB credentials and absolute paths. **Do not commit secrets**—use environment variables/secret store instead.
- `maven-compiler-plugin` is configured with `<skip>true/>`; CI/local builds may need `-Dmaven.compiler.skip=false` to force compilation.
- No unit tests detected in the repository—expect manual / integration verification when changing DB or core logic.

## How Copilot should help (concrete guidance) 🤖
- When changing DB schema: update `DB_design/ddl.sql` and `generatorConfig.xml` (then run MyBatis generator), and verify corresponding mapper XML under `src/main/resources/mapper_xml/`.
- When adding new REST endpoints: update backend controllers and swagger will reflect at `/jshERP-boot/doc.html` (can be used to generate frontend client calls).
- For frontend UI changes: prefer reusing existing `src/api/*.js` helpers and follow existing patterns in `src/views/*` and `src/components/*` (Ant Design Vue components + Vuex store when shared state is required).
- When touching plugin behavior: review both backend plugin entry points and `jshERP-web/src/views/system/*` for UI flows and API endpoints.

---
If any of the above is incorrect or you want more details in a specific area (CI, Docker, secrets, or local dev scripts), tell me which part to expand and I’ll update this file. ✅
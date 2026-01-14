# PR: feat(sts): STS provider & OssClientFactory

## Summary
Adds short-lived STS credentials integration and an OSS client factory to avoid long-lived AccessKey/Secret usage in the codebase. Refactors `OssUtils` to use the factory and updates frontend preview flows to request fresh presigned URLs.

## Changes
- `StsCredentialsProvider` (new)
- `OssClientFactory` (new)
- `OssUtils` (refactor)
- Frontend: Agency list/modal/detail preview fixes
- Unit tests for `StsCredentialsProvider`, `OssClientFactory` and `OssUtils`
- Removed embedded AccessKey/Secret from `application.yml` (switched to env vars)

## Migration & Security Notes
- Rotate any exposed AccessKeys immediately and update deployment environment variables.
- Recommended DB migration: convert any stored presigned URLs to `objectKey`.

## Testing
1. Compile and run tests in backend module:
   - `cd jshERP-boot && mvn -Dmaven.compiler.skip=false -DskipTests=false test`
2. Manually test frontend flows:
   - Upload a logo in Agency modal and confirm preview works instantly.
   - Check Agency list and detail pages that logos display correctly.

## CI / Follow-ups
- Fix duplicate `aliyun-sdk-oss` dependency in `pom.xml` (remove older/unused entry).
- Add/verify CI pipeline runs tests and fails on test failures.

## Reviewers
- @maintainer (please check security notes and deployment env var config)
- @frontend (please validate UI preview flows)

## Related Issues
- Links to any issue/ticket here.

---

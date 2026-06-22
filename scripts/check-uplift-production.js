const fs = require("fs");
const path = require("path");

const root = process.cwd();
const failures = [];

function read(relPath) {
  return fs.readFileSync(path.join(root, relPath), "utf8");
}

function mustMatch(relPath, pattern, message) {
  const text = read(relPath);
  if (!pattern.test(text)) failures.push(`${relPath}: ${message}`);
}

mustMatch("script.js", /const UPLIFT_LIMITS = Object\.freeze\(/, "limits must be centralized");
mustMatch("script.js", /favorites:\s*200/, "favorites must be capped");
mustMatch("script.js", /firestoreLimit\(UPLIFT_LIMITS\.favorites\)/, "favorite reads must be bounded");
mustMatch("script.js", /list\.replaceChildren\(\)/, "favorites must render through DOM nodes");
mustMatch("script.js", /button\.dataset\.text = encodeURIComponent\(f\.text\)/, "favorite removal must preserve encoded text");
mustMatch("script.js", /SUBMISSION_LAST_SENT_KEY/, "quote submissions must track local cooldown");
mustMatch("script.js", /submitCooldownMs:\s*60000/, "quote submissions must throttle repeated sends locally");
mustMatch("script.js", /normalizeSubmission\(\)/, "quote submissions must normalize bounded input");
mustMatch("script.js", /VALID_CATS\.includes\(cat\)/, "quote submissions must validate category");
mustMatch("script.js", /subWebsite/, "quote submissions must include a honeypot check");

mustMatch("index.html", /id="subText"[^>]+maxlength="500"/, "submitted quote text must have max length");
mustMatch("index.html", /id="subAuthor"[^>]+maxlength="120"/, "submitted quote author must have max length");
mustMatch("index.html", /id="subWebsite"/, "submitted quote form must include honeypot input");
mustMatch("style.css", /\.sub-hp\s*\{/, "honeypot input must be visually hidden");

mustMatch("firestore.rules", /function validUpliftFavorite\(/, "rules must validate favorite payloads");
mustMatch("firestore.rules", /data\.keys\(\)\.hasOnly\(\['quote_text', 'quote_author', 'quote_cat', 'created_at'\]\)/, "favorite rules must reject unexpected fields");
mustMatch("firestore.rules", /data\.created_at == request\.time/, "rules must require server timestamps");
mustMatch("firestore.rules", /function validUpliftSubmission\(/, "rules must validate quote submissions");
mustMatch("firestore.rules", /data\.submitted_by == request\.auth\.uid/, "submission rules must bind writer uid");
mustMatch("firestore.rules", /allow create: if signedIn\(\)\s*&& validUpliftSubmission\(request\.resource\.data\)/, "submissions must require auth and schema validation");

mustMatch(".github/workflows/quality.yml", /check-uplift-production\.js/, "quality workflow must run Uplift production checks");
mustMatch("scripts/check-repo-contracts.js", /check-uplift-production\.js/, "repo contracts must require Uplift production checks");

if (failures.length) {
  console.error("UPLIFT_PRODUCTION_CHECK_FAILED");
  for (const failure of failures) console.error(`- ${failure}`);
  process.exit(1);
}

console.log("UPLIFT_PRODUCTION_CHECK_OK");

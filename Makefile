.PHONY: branch-merge

branch-merge: ## Merge branch and close issue automatically
	@CURRENT_BRANCH=$$(git rev-parse --abbrev-ref HEAD); \
	ISSUE_NUMBER=$$(echo $$CURRENT_BRANCH | grep -o '^[0-9]\+' || echo ""); \
	BRANCH_TYPE=$$(echo $$CURRENT_BRANCH | grep -o '^[0-9]\+-\([^_]*\)' | cut -d'-' -f2 || echo ""); \
	VERSION=$$(echo $$CURRENT_BRANCH | grep -o '^[0-9]\+-[^_]*_\(.*\)' | cut -d'_' -f2 || echo ""); \
	echo "Branch: $$CURRENT_BRANCH"; \
	echo "Issue Number: $$ISSUE_NUMBER"; \
	echo "Branch Type: $$BRANCH_TYPE"; \
	echo "Description: $$VERSION"; \
	if [ -z "$$ISSUE_NUMBER" ]; then \
		echo "Error: Branch name must start with an issue number (e.g., 123-feature_description)"; \
		exit 1; \
	fi; \
	if [ -z "$$BRANCH_TYPE" ]; then \
		echo "Error: Branch name does not follow the pattern NNN-type_description"; \
		exit 1; \
	fi; \
	echo "Merging $$BRANCH_TYPE branch to dev..."; \
	git checkout dev || exit 1; \
	git pull origin dev || exit 1; \
	git merge $$CURRENT_BRANCH || exit 1; \
	git push origin dev || exit 1; \
	if [ -n "$$ISSUE_NUMBER" ]; then \
		echo "Adding commit to close issue #$$ISSUE_NUMBER..."; \
		git commit --allow-empty -m "Close #$$ISSUE_NUMBER"; \
		git push origin dev || exit 1; \
	fi; \
	echo "Cleaning up..."; \
	echo "Deleting local branch $$CURRENT_BRANCH..."; \
	git branch -D "$$CURRENT_BRANCH" || exit 1; \
	echo "Deleting remote branch origin/$$CURRENT_BRANCH..."; \
	git push origin --delete "$$CURRENT_BRANCH" || true; \
	echo "Branch merge and cleanup completed successfully!"

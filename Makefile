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
		echo "\033[0;31mError: Branch name must start with an issue number (e.g., 123-feature_description)\033[0m"; \
		exit 1; \
	fi; \
	if [ -z "$$BRANCH_TYPE" ]; then \
		echo "\033[0;31mError: Branch name does not follow the pattern NNN-type_description\033[0m"; \
		exit 1; \
	fi; \
	echo "\033[0;33mMerging $$BRANCH_TYPE branch to master...\033[0m"; \
	git checkout master || exit 1; \
	git pull origin master || exit 1; \
	git merge $$CURRENT_BRANCH || exit 1; \
	git push origin master || exit 1; \
	if [ -n "$$ISSUE_NUMBER" ]; then \
		echo "\033[0;33mAdding commit to close issue #$$ISSUE_NUMBER...\033[0m"; \
		git commit --allow-empty -m "Close #$$ISSUE_NUMBER"; \
		git push origin master || exit 1; \
	fi; \
	echo "\033[0;33mCleaning up...\033[0m"; \
	echo "Deleting local branch $$CURRENT_BRANCH..."; \
	git branch -D "$$CURRENT_BRANCH" || exit 1; \
	echo "Deleting remote branch origin/$$CURRENT_BRANCH..."; \
	git push origin --delete "$$CURRENT_BRANCH" || true; \
	echo "\033[0;32mBranch merge and cleanup completed successfully!\033[0m"

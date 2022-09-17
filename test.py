import github
import asyncio

username="typicalitguy"
token="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"

async def main():
    client = await github.GHClient(username=username,token=token,user_cache_size=30,repo_cache_size=15,custom_headers={})
    user = await client.get_user(user=username)
    repos = await user.repos()
    for repo in repos:
        print(repo.url)
    # repo = await client.get_repo(owner=username,repo="Algorithm-and-data-structure-in-java'")
    # print(repo)
    await client.close()

asyncio.run(main())
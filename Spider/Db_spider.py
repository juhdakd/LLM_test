import requests
from bs4 import BeautifulSoup

root_url = "https://movie.douban.com/top250"

fopen=open("movie.txt","w")
page_index = range(0, 25, 25)  # 包含250个电影的索引范围
list(page_index)
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'}
for idx in page_index:
    url = f"{root_url}?start={idx}&filter="
    print(url)
    r = requests.get(url,headers=headers)
    # 检查请求是否成功
    if r.status_code == 200:
        soup = BeautifulSoup(r.text, 'html.parser')
        # 打印出来看看是否正确找到了article div
        article_div = soup.find('div', class_="article")
        if article_div:
            ol_grid_view = article_div.find('ol', attrs={"class": "grid_view"})
            if ol_grid_view:
                movie_name_items = ol_grid_view.find_all('div', attrs={"class": "info"})
                for item in movie_name_items:
                    link = item.find('a')
                    title= link.find('span', attrs={"class": "title"}).text
                    star= item.find('div',class_="star").find('span',class_="rating_num").text
                    print("%s\t%s\t%s\n"%(title,star,link.get("href")))
                    fopen.write("%s\t%s\t%s\n"%(title,star,link.get("href")))
        else:
            print("No article div found.")
    else:
        print(f"Failed to retrieve content from {url}, status code: {r.status_code}")

fopen.close()
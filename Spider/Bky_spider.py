import requests
from bs4 import BeautifulSoup

root_url="https://www.suredian.com/?source=jianfast"
headers={"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36"}
rlink=requests.post(root_url,data={"res":"baidu"},headers=headers)
print(rlink.text)
soup=BeautifulSoup(rlink.text,"html.parser")

news_links=soup.find_all('div','news-box')

for link in news_links:
    news_title=link.find('span','news-title').text
    news_text=link.find('span','news-excerpt').text
    news_link=link.find('a').get('href')
    print("%s\t%s\t%s"%(news_title,news_text,news_link))
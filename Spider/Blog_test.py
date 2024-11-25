import requests
from bs4 import BeautifulSoup
from Utils import url_manager
import re
# url="http://www.crazyant.net"

# r=requests.get(url)

# if(r.status_code!=200):
#     raise Exception("error")

# html_docs=r.text

# soup=BeautifulSoup(html_docs,"html.parser")

# h2_nodes=soup.find_all("h2",class_="entry-title")

# for h2_node in h2_nodes:
#     link=h2_node.find("a")
#     print(link.get("href"),link.get_text())

root_url="http://www.crazyant.net"

fout = open("C:\\Users\\22388\\Desktop\\LLM\\Spider\\blog.txt", 'w')

url_manager=url_manager.Urlmanager()
url_manager.add_new_url(root_url)
while url_manager.has_new_url():
    current_url=url_manager.get_new_url()
    r=requests.get(current_url,timeout=3)
    if(r.status_code!=200):
        print("error",current_url)
        continue
    soup=BeautifulSoup(r.text,"html.parser")
    title=soup.title.get_text()
    fout.write("%s\t%s\n"%(current_url,title))
    print("%s\t%s\n"%(current_url,title))

    links=soup.find_all("a")
    for link in links:
       href=link.get("href")
       pattern=r'^http://www.crazyant.net/\d+.html$'
       if re.match(pattern,href):
           url_manager.add_new_url(href)
fout.close()



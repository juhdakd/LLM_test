class Urlmanager():
    # url管理器

    def __init__(self):
        self.new_urls = set() #待爬取的url
        self.old_urls = set() #已爬取的url
    

    # 新增url并判重
    def add_new_url(self, url):
        if url is None or len(url) == 0:
            return
        if url in self.new_urls or url in self.old_urls: #已经存在
            return
        if url not in self.new_urls and url not in self.old_urls:
            self.new_urls.add(url)
    # 批量添加url
    def add_new_urls(self, urls):
        if urls is None or len(urls) == 0:
            return
        for url in urls:
            self.add_new_url(url)
    
    # 获取代爬取url
    def get_new_url(self):
        if self.has_new_url():
            url= self.new_urls.pop()
            self.old_urls.add(url)
            return url
        else:
            return None
    # 判断是否还有待爬取url
    def has_new_url(self):
        return len(self.new_urls) != 0
    

if __name__ == '__main__':
    urlmanager = Urlmanager()
    urlmanager.add_new_url('http://www.baidu.com')
    urlmanager.add_new_url('http://www.baidu.com')
    urlmanager.add_new_url('http://www.sina.com')
    print(urlmanager.new_urls,urlmanager.old_urls)

    print("#")
    new_url = urlmanager.get_new_url()
    print(urlmanager.new_urls,urlmanager.old_urls)

    print("#")
    new_url = urlmanager.get_new_url()
    print(urlmanager.new_urls,urlmanager.old_urls)


    print(urlmanager.has_new_url())


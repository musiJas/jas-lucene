# -*- coding:UTF-8 -*-
from bs4 import BeautifulSoup
from urllib.request import urlopen
import requests, sys
import redis
 
class component(object):
    titles=''

class downloader(object):

    def __init__(self):
        self.server = 'https://www.cnblogs.com/#p'
        self.target = 'https://www.cnblogs.com/'
        self.pages=[]
        self.titles = []          
        self.urls = []
        self.auths=[]
        self.dates=[]
        self.digs=[]
        self.comments=[]
        self.browse=[]
        self.content=[]
        self.imgs=[]
        self.redis={}

    def initTotalPage(self):
        for x in range(200):
            self.pages.append(self.server+str(x))
 
    def get_content_url(self):
        self.initTotalPage()
        for  url   in  self.pages:
            html = urlopen(url)
            div_bf = BeautifulSoup(html.read(),"html.parser")
            # req = requests.get(url = self.target)
            # html = req.text
            # div_bf = BeautifulSoup(html,features='html.parser')
            div = div_bf.find_all('section', class_ = 'post-item-body')
            # print(len(div[15:]))
            for  section   in  div:
                a_bf = BeautifulSoup(str(section),"html.parser")
                links = a_bf.find_all('a')
                # self.nums = len(a[15:]) 
                self.titles.append(links[0].string)
                self.urls.append(links[0].get('href'))
                self.auths.append( links[1].span.string)
                spans=a_bf.find_all('span',class_ ='post-meta-item')
                self.dates.append(spans[0].span.string)
                self.digs.append(links[2].span.string)
                self.comments.append(links[3].span.string)
                self.browse.append(links[4].span.string)
                p=a_bf.find_all('p',class_ ='post-item-summary')
                cont=str(p[0])
                self.content.append(cont[cont.index('>'):])
                img=a_bf.find_all('img',class_ ='avatar')
                if len(img) > 0:
                    self.imgs.append(img[0].get('src'))
                else:
                    self.imgs.append('')    
                
                # self.names.append(each.string)
                # self.urls.append(self.server + each.get('href'))
       
    # def get_contents(self, target):
    #     req = requests.get(url = target)
    #     html = req.text
    #     bf = BeautifulSoup(html)
    #     texts = bf.find_all('div', class_ = 'showtxt')
    #     texts = texts[0].text.replace('\xa0'*8,'\n\n')
    #     return texts
    def initialRedisPool(self):
        pool = redis.ConnectionPool(host='94.191.4.62',password='root', port=6379, decode_responses=True) 
        r = redis.Redis(connection_pool=pool)
        self.redis=r

    def writerRedis(self):
        self.initialRedisPool()
        redis=self.redis
        for  index  in range(len(self.urls)):
            name ='html'
            print(self.urls[index])
            # 先判断key 是否存在如果存在则跳过
            if redis.hexists(name, self.urls[index]):
                continue
            else:
                component = {
                    'titles':self.titles[index],
                    'urls' : self.urls[index],
                    'auths':self.auths[index],
                    'dates' :self.dates[index],
                    'digs' :self.digs[index],
                    'comments':self.comments[index],
                    'browse' : self.browse[index],
                    'content':self.content[index],
                    'imgs':self.imgs[index]
                    }
                # component=self.titles[index]+'@'+self.urls[index]+'@'+self.auths[index]+'@'+self.dates[index]+'@'+self.digs[index]+'@'+self.comments[index]+'@'+self.browse[index]+'@'+self.content[index]+'@'+self.imgs[index]
                redis.hset(name, self.urls[index], str(component))
            

if __name__ == "__main__":
    print('start ../')
    dl = downloader()
    # dl.initTotalPage()
    dl.get_content_url()
    dl.writerRedis()
    print('end...')
  
    # dl.get_content_url()
   

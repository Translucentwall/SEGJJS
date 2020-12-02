from scrapy import Request
from scrapy.spiders import Spider
from selenium import webdriver
import time
from lunwenspider.items import lunwenspiderItem
import csv
import json
import re
import requests
class TSESpider(Spider):
    name="tse"

    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)',
    }

    cookies={'dblp-view':'y','dblp-search-mode':'c','dblp-bibtex':'2','dblp-dismiss-new-feature-2019-08-19':'true'}

    url="https://dblp.uni-trier.de/db/journals/tse/index.html"

    def start_requests(self):
        yield Request(self.url,cookies=self.cookies,headers=self.headers,callback=self.menuParse)

    def menuParse(self,response):
        volums=response.xpath('//div[@id="main"]/ul/li/a/@href').extract()
        for volum in volums:
            headers={
                'Refer':volum,
                'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)',
            }
            yield Request(volum, cookies=self.cookies, headers=headers, callback=self.volumParse)
        # for i in range(0,11):
        #     headers={
        #         'Refer':volums[i],
        #         'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)',
        #     }
        #     yield Request(volums[i], cookies=self.cookies, headers=headers, callback=self.volumParse)
        # index=0
        # headers = {
        #       'Refer':volums[index],
        #       'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)',
        #      }
        # yield Request(volums[index], cookies=self.cookies, headers=headers, callback=self.volumParse)

    def volumParse(self,response):
        papers=response.xpath('//div[@id="main"]/ul[@class="publ-list"]/li[@class="entry article"]/nav[@class="publ"]/ul/li[1]/div[@class="head"]/a/@href').extract()
        cookies={
            's_ecid':'MCMID%7C11455480075107030443759685347458698804',
            'fp':'c3bf885571408b916916b75bf668c4f8',
            's_fid':'6617BCD95A617C78-0FB292CE01829215',
            '__gads':'ID=7940aabbc11f63d2:T=1602668107:S=ALNI_Ma4IQhV36lNj92Tu-50-4YIM-2xxA',
            'AMCVS_8E929CC25A1FB2B30A495C97%40AdobeOrg':'1',
            's_cc':'true',
            'ipList':'64.62.184.12',
            'WLSESSION':'203580044.20480.0000',
            'AMCV_8E929CC25A1FB2B30A495C97%40AdobeOrg':'1687686476%7CMCIDTS%7C18158%7CMCMID%7C11455480075107030443759685347458698804%7CMCAAMLH-1603282289%7C9%7CMCAAMB-1603282289%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1602684689s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C3.0.0',
            's_sq':'ieeexplore.prod%3D%2526c.%2526a.%2526activitymap.%2526page%253DDynamic%252520html%252520doc%252520detail%2526link%253DSign%252520Out%2526region%253DLayoutWrapper%2526pageIDType%253D1%2526.activitymap%2526.a%2526.c%2526pid%253DDynamic%252520html%252520doc%252520detail%2526pidt%253D1%2526oid%253Dhttps%25253A%25252F%25252Fieeexplore.ieee.org%25252Fservlet%25252FLogin%25253Flogout%25253D%25252FXplore%25252Fguesthome.jsp%2526ot%253DA',
            'JSESSIONID':'KzsnCQe0MY6BvU35-vftTLj6mdskYpur8E4JBze5cYn3MXBF9ECv!1820523694',
            'ipCheck':'64.62.184.12',
            'TS01b03060':'012f35062386639b86569c27a19b37036f547e57e27b5948049612fe44f1a6d87f6c485bf412477e62cc5e081b7c64c414038c255f991b61eb340e54505a3461bf4fa25b4b31e9d827e00753a07c5813ca23e03a6b9096c0f46f9945f73844d5fa40210ac9d3347ef2cd5ff81a42bcc8f6a7ace3fa673dd35375fa621a20deda211fb92ce235625a90b3addef591457d93f7d55e39548ebc45f74e5d672a90a194a3de0b5d4383ce00ade541b4fc9bc04a4e560f6c920acc91100d773c3a80b6d834b1ad05',
            'utag_main':'v_id:016d44249e4300033f860d252b4703072003606a007e8$_sn:14$_ss:0$_st:1602679540053$vapi_domain:ieee.org$_se:7$ses_id:1602677479745%3Bexp-session$_pn:4%3Bexp-session',
            'TS01b03060_26':'TS01b03060_26=014082121d5b8e1d9d10dff5cb871229b6d3b8241e2e713732501eaeb97089734cfbb5d38dfac3a7b5c6f63cd6bebb3f7d3a0ee2c09832bf401458e350aa3ad5a6233d48f2',
        }
        for paper in papers:
            headers = {'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)'}
            item=lunwenspiderItem()
            try:
                res = requests.get(paper, headers=headers,timeout=240)
            except Exception:
                continue
            url=res.url
            pattern = re.compile('metadata={.*};')
            content = json.loads(pattern.search(res.text).group()[9:-1])
            res.close()
            if 'title' in content:
                title = content['title']
            else:
                title = 'unknown'
            if 'citationCount' in content:
                citationCount=content['citationCount']
            else:
                citationCount=0
            if 'publicationTitle' in content:
                publication = content['publicationTitle']
            else:
                publication = 'unknown'

            if 'publicationDate' in content:
                time=content['publicationDate']
            else:
                time='unkown'

            if 'keywords' in content:
                keywords = self.get_keywords(content['keywords'])
            else:
                keywords = list()

            if 'doi' in content:
                doi = content['doi']
            else:
                doi = 'unknown'

            lists=[]
            if 'authors' in content:
                authors=content['authors']
                for i in authors:
                    name=i['name']
                    if 'affiliation' in i:
                        affiliation=i['affiliation'][0]
                    else:
                        affiliation="unknow"
                    author={
                        "name":name,
                        "affiliation":affiliation
                    }
                    lists.append(author)

            if 'abstract' in content:
                abstract=content['abstract']
            else:
                abstract=""

            item['doi']=doi
            item['title']=title
            item['publisher']=publication
            item['keyword']=keywords
            item['abstract']=abstract
            item['author']=lists
            item['time']=time
            item['citationCount']=citationCount
            options = webdriver.ChromeOptions()
            # options.binary_location = GOOGLE
            options.add_argument('--headless')
            options.add_argument('blink-settings=imagesEnabled=false')
            options.add_argument('--disable-gpu')
            options.add_argument("--no-sandbox")
            driver = webdriver.Chrome(chrome_options=options,
                                      executable_path='C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe')
            # driver = webdriver.Chrome(chrome_options=options)
            driver.set_page_load_timeout(60)
            try:
                driver.get(url + "references")
                time.sleep(5)
            except Exception:
                pass
            li = []
            try:
                references = driver.find_element_by_id("references-section-container")
                references = references.find_elements_by_class_name("reference-container")
                for reference in references:
                    tmp = reference.find_elements_by_tag_name("span")
                    text = str(tmp[1].get_attribute('innerText'))
                    if "\"" in text:
                        parts = text.split(",")
                        label = 0
                        for i in range(0, len(parts)):
                            if ("\"" in parts[i]):
                                label = i
                                break
                        authors = []
                        title = parts[label]
                        for i in range(0, label):
                            part = parts[i]
                            if "et al" in part:
                                part = part.replace("et al", "")
                            if "and" in part:
                                tmp = part.split("and")
                                if(len(tmp[0])>1):
                                    authors.append(tmp[0])
                                authors.append(tmp[1])
                            else:
                                authors.append(part)
                    else:
                        continue
                    a = {
                        "title": title,
                        "authors": authors
                    }
                    li.append(a)
            except Exception:
                references = li
            item['references'] = li
            yield item
            # yield Request(paper, meta={'item': item}, cookies=cookies, headers=headers, callback=self.paperParse)

    def paperParse(self,response):
        item = response.meta['item']
        options = webdriver.ChromeOptions()
        # options.binary_location = GOOGLE
        options.add_argument('--headless')
        options.add_argument('blink-settings=imagesEnabled=false')
        options.add_argument('--disable-gpu')
        options.add_argument("--no-sandbox")
        driver = webdriver.Chrome(chrome_options=options, executable_path='C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe')
        # driver = webdriver.Chrome(chrome_options=options)
        # driver.set_page_load_timeout(120)
        try:
            driver.get(response._url+"references")
            time.sleep(5)
        except Exception:
            pass
        try:
            answer=""
            li=[]
            references=driver.find_element_by_id("references-section-container")
            references=references.find_elements_by_class_name("reference-container")
            for reference in references:
                tmp=reference.find_elements_by_tag_name("span")
                text=str(tmp[1].get_attribute('innerText'))
                if "\"" in text:
                    parts=text.split(",")
                    label=0
                    for i in range(0,len(parts)):
                        if("\"" in parts[i]):
                            label=i
                            break
                    authors=[]
                    title=parts[label]
                    for i in range(0,label):
                        part=parts[i]
                        if "et al" in part:
                            part=part.replace("et al","")
                        if "and" in part:
                            tmp=part.split("and")
                            authors.append(tmp[0])
                            authors.append(tmp[1])
                        else:
                            authors.append(part)
                else:
                    continue
                a={
                    "title":title,
                    "authors":authors
                }
                li.append(a)
        except Exception:
            references=""
        item['references']=li
        yield  item
        # try:
        #     title=driver.find_element_by_class_name("document-title").text
        # except Exception:
        #     title=""
        #
        #
        # try:
        #     absatract = driver.find_elements_by_class_name("u-mb-1")[2]
        #     absatract=absatract.text
        # except Exception:
        #     absatract = ""
        #
        # try:
        #     doi=driver.find_elements_by_class_name("u-pb-1 stats-document-abstract-doi")
        # except Exception:
        #     doi=""
        #
        # try:
        #     publisher=driver.find_elements_by_class_name("publisher-info-container black-tooltip")
        # except Exception:
        #     publisher=""

        print(response._url)

    def get_keywords(self,keywords):
        for kds in keywords:
            if kds["type"] == 'IEEE Keywords':
                return kds["kwd"]
        return []

    # def get_reference(self,url):
    #     headers = {"Connection": "close", "Accept": "application/json, text/plain, */*", "cache-http-response": "true",
    #                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3377.1 Safari/537.36",
    #                "Referer": url+ "references",
    #                "Accept-Encoding": "gzip, deflate",
    #                "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"}
    #     res = requests.get(url+ "references", headers=headers)
    #     txt=res.text
    #     pattern = re.compile('metadata={.*};')
    #     temp=pattern.search(res.text).group()
    #     # content = json.loads(pattern.search(res.text).group())
    #
    #     if 'references' in content:
    #         reference = content["references"]
    #
    #     else:
    #         reference = list()
    #     return reference
    #
    # def get_author(self,url):
    #     headers = {"Connection": "close", "Accept": "application/json, text/plain, */*", "cache-http-response": "true",
    #                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3377.1 Safari/537.36",
    #                "Referer": url + "authors",
    #                "Accept-Encoding": "gzip, deflate",
    #                "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"}
    #     res = requests.get(url + "authors", headers=headers)
    #     content = json.loads(res.text)
    #     print(content)
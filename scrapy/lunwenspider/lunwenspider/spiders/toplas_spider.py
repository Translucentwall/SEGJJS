from scrapy import Request
from scrapy.spiders import Spider
from lunwenspider.items import lunwenspiderItem

class TOPLASSpider(Spider):
    name="toplas"

    USER_AGENTS = [
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
        "Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
        "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
        "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
        "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
        "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)",
        "Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
        "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
        "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
        "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52"]
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)',
        'referer': 'https://dl.acm.org/action/doSearch?fillQuickSearch=false&expand=dl&field1=AllField&SeriesKeyAnd=TOPLAS&AfterMonth=1&AfterYear=2000&BeforeMonth=12&BeforeYear=2020',
    }
    cookies={
        '_ga':'GA1.2.957704674.1567428228',
        'MAID':'9QnZe2jWj+llENHcxBGyPQ==',
        'Pastease.passive.chance.5YhMrk04JDZQkJe':'chance9.9',
        'Pastease.passive.activated.5YhMrk04JDZQkJe': '0',
        'cookiePolicy': 'accept',
        '_hjid': 'b5dc5ef6-a835-4a06-b2ee-9d462e384d81',
        '__atssc': 'google%3B9',
        '__cfduid': 'd920d353f0b81e5df9e12cad470207ae31600774919',
        '_gid': 'GA1.2.262679785.1601897603',
        '__atuvc': '4%7C37%2C5%7C38%2C4%7C39%2C0%7C40%2C5%7C41',
        'JSESSIONID': 'c28c761d-f08a-4bfc-b7c5-d9d277907264',
        'SERVER': 'WZ6myaEXBLHieil4TSa9ig==',
        'MACHINE_LAST_SEEN': '2020-10-05T22%3A41%3A59.939-07%3A00',
        '_hp2_ses_props.1083010732': '%7B%22r%22%3A%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DCdDvwD2uLS65j0fcUo2ImCJ7gxIrPOev3H6xt_rhv_m%26wd%3D%26eqid%3De741f5270000e5d3000000055f7c03a2%22%2C%22ts%22%3A1601962923134%2C%22d%22%3A%22dl.acm.org%22%2C%22h%22%3A%22%2F%22%7D',
        '_hjTLDTest': '1',
        '_hjAbsoluteSessionInProgress': '0',
        '_gat_UA-76155856-1': '1',
        '_hp2_id.1083010732': '7B%22userId%22%3A%227764947466729827%22%2C%22pageviewId%22%3A%222688742004293378%22%2C%22sessionId%22%3A%227707955095552593%22%2C%22identity%22%3Anull%2C%22trackerVersion%22%3A%224.0%22%7D',
        '_gali': 'pb-page-content',
    }

    url=\
    "https://dl.acm.org/action/doSearch?fillQuickSearch=false&expand=dl&field1=AllField&SeriesKeyAnd=TOPLAS&AfterMonth=1&AfterYear=2000&BeforeMonth=12&BeforeYear=2020&startPage=0&ContentItemType=research-article&pageSize=20"


    def start_requests(self):
        yield Request(self.url,cookies=self.cookies,headers=self.headers,callback=self.tableParse)

    def tableParse(self,response):
        for i in range(0,12):
            header={
                'referer': 'https://dl.acm.org/action/doSearch?fillQuickSearch=false&expand=dl&field1=AllField&SeriesKeyAnd=TOPLAS&AfterMonth=1&AfterYear=2000&BeforeMonth=12&BeforeYear=2020',
                'User-Agent':self.USER_AGENTS[i%15]
            }
            url="https://dl.acm.org/action/doSearch?fillQuickSearch=false&expand=dl&field1=AllField&SeriesKeyAnd=TOPLAS&AfterMonth=1&AfterYear=2000&BeforeMonth=12&BeforeYear=2020&startPage="+str(i)+"&ContentItemType=research-article&pageSize=20"
            yield Request(url, cookies=self.cookies, headers=header, callback=self.parse)

    def parse(self, response):

        searchItems=response.xpath(
                '//li[@class="search__item issue-item-container"]/div[@class="issue-item issue-item--search clearfix"]/div[@class="issue-item__content"]/div[@class="issue-item__content-right"]/h5[@class="issue-item__title"]/span[@class="hlFld-Title"]/a/@href').extract()
        pre="https://dl.acm.org"
        for searchItem in searchItems:
            header={
                'Referer': pre+searchItem,
                'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36',
            }
            yield Request(pre+searchItem,headers=header,callback=self.websiteParse)


    def websiteParse(self,response):
        item=lunwenspiderItem()
        item['title']=response.xpath('//div[@class="border-bottom clearfix"]/h1/text()').extract()[0]
        citation=response.xpath('//div[@class="border-bottom clearfix"]/div[@class="issue-item__footer"]/div[@class'
                                '="issue-item__footer-info pull-left"]/div/ul/li/span/span[1]/text()').extract()[0]
        abstracts= response.xpath('//div[@class="abstractSection abstractInFull"]/p/text()').extract()
        publication=response.xpath('//div[@class="border-bottom clearfix"]/div[@class="issue-item__detail"]'
                                   '/a/span/text()').extract()
        time=response.xpath('//div[@class="border-bottom clearfix"]/div[@class="issue-item__detail"]'
                                   '/span[@class="dot-separator"][1]/span/text()').extract()[0]
        item['time']=time.strip()
        item['keyword']=["Programming Languages and Systems"]
        text=""
        for abstract in abstracts:
            text=text+abstract+"\n"
        item['abstract']=text
        authors=response.xpath('//div[@id="sb-1"]/ul/li[@class="loa__item"]/a/span[@class="loa__author-info"]/div[@class="author-data"]/span[@class="loa__author-name"]/span/text()').extract()
        organizations=response.xpath('//div[@id="sb-1"]/ul/li[@class="loa__item"]/a/span[@class="loa__author-info"]/span[@class="loa_author_inst"]/p/text()').extract()
        authorInfo=""
        orgInfo=""
        list=[]
        for i in range(0,len(authors)):
            author = {
                "name": authors[i],
                "affiliation": organizations[i]
            }
            list.append(author)
        item['author']=list
        item['publisher']=publication[0]
        item['citationCount'] =citation
        # references=response.xpath('//div[@class="article__section article__references show-more-items js--showMore"]/ol[@class="rlist references__list references__numeric"]/li/span/text()').extract()
        references = response.xpath(
            '//div[@class="article__body article__abstractView"]/div[@class="article__section article__references show-more-items"]/ol[@class="rlist references__list references__numeric"]/li/span/text()').extract()
        refers=[]
        for reference in references:
            try:
                a = 0
                for i in range(0, len(reference)):
                    if (reference[i].isdecimal()):
                        a = i
                        break
                b = a
                for i in range(a, len(reference)):
                    if (reference[i] == '.'):
                        a = i
                        break
                s = ""
                for i in range(a + 2, len(reference)):
                    if (reference[i] == "."):
                        break
                    s = s + reference[i]
                infos = reference[0:b - 2]
                tmp = infos.split(",")
                authors = []
                for i in tmp:
                    if ('.' in i):
                        continue
                    if (len(i) <= 2):
                        continue
                    if (i.endswith("et al")):
                        i = i.replace("et al", "")
                    if ("and" in i):
                        tp = i.split("and")
                        if (len(tp[0]) > 1):
                            authors.append(str(tp[0]).replace("and",""))
                    else:
                        authors.append(i)
                a = {
                    "title": s,
                    "authors": authors
                }
                refers.append(a)
            except:
                continue
        item['references']=refers
        yield item



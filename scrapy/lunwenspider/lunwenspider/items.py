# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class lunwenspiderItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    # 标题
    title = scrapy.Field()
    # 作者
    author = scrapy.Field()
    # # 组织
    # organization = scrapy.Field()
    # 摘要
    abstract = scrapy.Field()
    # 关键词
    keyword = scrapy.Field()
    # # 编号
    # number = scrapy.Field()
    # # 杂志
    # magazine = scrapy.Field()
    # 引用
    references = scrapy.Field()
    # DOI
    doi = scrapy.Field()
    #出版商
    publisher= scrapy.Field()

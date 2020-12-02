from scrapy import cmdline


# name = 'tse'
# name="ieee_journals"
name="toplas"
# cmd = 'scrapy crawl {0} -o tse.json'.format(name)
# cmd = 'scrapy crawl {0} -o ieee_journals.json'.format(name)
cmd = 'scrapy crawl {0} -o toplas.json'.format(name)
# -o douban.csv -o zhiwang.json
cmdline.execute(cmd.split())
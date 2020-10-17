from scrapy import cmdline


name = 'tse'
cmd = 'scrapy crawl {0} -o tse.csv'.format(name)
# -o douban.csv -o zhiwang.json
cmdline.execute(cmd.split())
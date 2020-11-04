from scrapy import cmdline


name = 'tse'
cmd = 'scrapy crawl {0} -o tse.json'.format(name)
# -o douban.csv -o zhiwang.json
cmdline.execute(cmd.split())
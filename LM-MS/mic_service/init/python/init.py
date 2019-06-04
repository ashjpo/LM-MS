#-*- coding:utf-8 -*-
import json
import hprose

class test:
    def hello(self,name):
        return 'Hello %s!' % name



def main():

    server = hprose.HttpServer(host="tcp://0.0.0.0",port = 8181)

    server.addMethod("hello",test())

    server.start()



if __name__ == '__main__':

    main()

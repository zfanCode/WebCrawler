# Zhihu Web Crawler

This project is to crawl all questions on www.zhihu.com as a practice of Networking in Java.

#There are two parts:
1. master (only one)
2. slave (multiple)


# Master's Responsibility

##Variable: 
    An integer set that contains all urls (or question ids) of added questions on Zhihu.com.

##Threads:
    One thread for receiving ids from the slaves and putting the ids into a queue.
    Another thread is for populating the ids to each slaves so that they have urls to download and their workload can be balanced.



# Slave's Responsibility

##Threads:
    One is for website downloading. 
    Another one is for extracting urls and sending back the extracted question ids to the master.


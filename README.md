## Zhihu Web Crawler

This project is to crawl all questions on www.zhihu.com as a practice of Networking in Java.

The program consists of two parts:
1. master (only one)
2. slave (multiple)


### Master's Outline

####Variable
- UniverseSet: Set<Integer>

> An integer set that contains all urls (or question ids) of added questions on Zhihu.com.

-  populatingQueue: Queue<Integer>

#### Threads
- ExtractedIdAcceptingService

>One thread for receiving ids from the slaves and putting the ids into a queue.

- PopulatingService

>Another thread is for populating the ids to each slaves so that they have urls to download and their workload can be balanced.

#### Class
1. ExtractedIdAcceptingService.java
2. PopulatingService.java
3. Master.java


### Slave's Outline

#### Varibale
- pendingIds: Queue<Integer>

#### Threads
- DownloaderService
-  HTMLParser
-  IdAcceptingService

#### Class
1. DownloadService.java
2. HTMLSoup.java
3. IdAcceptService.java
4. Slave.java

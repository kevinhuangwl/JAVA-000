# 1.（选做）列举常用的并发操作 API 和工具类，简单分析其使用场景和优缺点。
并发工具类按使用场景，可以分为以下几类

## 分工
### Thread
场景：写demo时用  
优点：简单，用完自行销毁  
缺点：耗时，创建效率低

### ThreadPoolExecutor
场景：生产级代码，处理一般任务  
优点：线程复用  
缺点：容易忽略对任务队列与拒绝策略的设置而导致线上问题  

### ScheduledThreadPoolExecutor  
场景：生产级代码，处理定时任务  
优点：自动计时  
缺点：暂时没有想到

## 协作
### Semaphore


### CountDownLatch


### CyclicBarrier

### FutureTask
### CompletableFuture

## 互斥
### synchronize
### ReentrantLock
### ThreadLocal
场景：用于寄存线程独占的对象，使对象不会产生线程安全问题；   
优点：不会产生线程安全问题；  
缺点：容易忘记清理残留对象；由于是隐式传参，代码不容易发现问题

### ConcurrentHashMap


### ConcurrentLinkedList

### ArrayBlockingQueue


# 2.（选做）请思考：什么是并发？什么是高并发？实现高并发高可用系统需要考虑哪些因素，对于这些你是怎么理解的？
 
# 3.（选做）请思考：还有哪些跟并发类似 / 有关的场景和问题，有哪些可以借鉴的解决办法。

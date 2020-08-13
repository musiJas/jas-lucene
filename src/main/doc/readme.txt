1.关于执行脚本，在linux平台下运行异常问题，是因为，脚本是在windows平台下书写的，文件编码是dos的
需要修改
修改步骤：
    vim  startup.sh
    :set ff   ( 查看编码  如果显示的dos的
    :set ff=unix
    :wq!
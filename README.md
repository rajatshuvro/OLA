# OLA
Onkur Library Application

The aim of this app is to automate Onkur Bangla Language Academy's library management. Major use cases are:
- Adding new books and users to the library.
- Book checkout/return.
- Maintain a database of book transactions that can be queried.
- Enable automated queries on books and users.

## Command Line Interface

Usage:<br/>
<pre><code>
	[ola -b {book file path} -u {users file path} -t {transactions file path}]
</pre></code>

Once OLA starts successfully, the following sub-commands will be available<br/>

<pre><code>
	add            (add new books/users)<br/>
	add-user/au    (add new user via command line)<br/>
	co             (checkout book)<br/>
	ret            (return book)<br/>
	co-stat/cs     (checkout status)<br/>
	search/$       (free text search on books and user records)<br/>
	filter         (filter book database by genre, level, etc fields)<br/>
	legacy         (import books from legacy tsv)<br/>
	quit           (quit OLA)<br/>
	help           (print this menu)<br/>
	[Type command to get detailed help]<br/>
</pre></code>

##How To:
###Search for a user or book:
User and book records are searchable records in OLA. This is a free text search, i.e. you can look for any text. 
This is very useful for looking up book or user records (that contains their ids) during checkout, return or for checkout status query.
###Checkout a book:
Please use the 'co' command. If you need to lookup the user id or book id, please use the 'search' command.
<br/>
###Return a book:
Please use the 'ret' command.
###Add books/users:
We provide Google forms to make book/user entries. 
Entries can then be downloaded as CSV files and imported via the 'add' command.
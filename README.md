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
	filter         (filter book database by genre and/or reading level)<br/>
	label          (print out book titles and Ids)<br/>
	quit           (quit OLA)<br/>
	help           (print this menu)<br/>
	[Type command to get detailed help]<br/>
</pre></code>

## How To:

### Search for a user or book:
User and book records are searchable in OLA. This is a free text search, i.e. you can look for any text. 
This is very useful for looking up book or user records (that contains their ids) during checkout, return or for checkout status query.

### Filter books:
This is a more accurate (but limited) way of searching for book records by genre and/or reading level. 
Although free text search is very powerful, but it may return irrelevant entries while searching by reading level and genre.
The filter command is more appropriate in this case. 

### Checkout a book:
Please use the <code>co</code> command. If you need to lookup the user id or book id, please use the <code>search</code> command.

### Return a book:
Please use the <code>ret</code> command.

### Add books/users:
We provide Google forms to make book/user entries. 
Entries can then be downloaded as CSV files and imported via the <code>add</code> command.

### Add a user:
If you wish to add a user via the CLI, please use the <code>add-user/au</code> command.

### Check user/book status:
To check how many books a user has checked out or if a book is in stock, please use the <code>co-stat/cs</code> command. 
You can specify a user id, or a book id or ISBN. 
When ISBN is provided, status for all copies of books with that ISBN number will be reported.

### Print labels:
Print out labels/tags for books with the title and user friendly book id.
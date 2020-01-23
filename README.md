# OLA
Onkur Library Application

The aim of this app is to automate Onkur Bangla Language Academy's library management. Major use cases are:
- Adding/removing new books to the library.
- Book checkout/checkin.
- Maintain a database of book transactions.
- Enable automated queries.

## Command Line Interface

Usage:<br/>
<pre><code>
	[ola -b {book file path} -u {users file path} -t {transactions file path}]
</pre></code>

Once OLA starts successfully, the following sub-commands will be available<br/>

<pre><code>
	add-books      (add new books)<br/>
	add-user       (add new user via command line)<br/>
	co             (checkout book)<br/>
	ret            (return book)<br/>
	co-stat        (checkout status)<br/>
	search         (free text search on books, users and transactions)<br/>
	filter         (filter book database by genre, level, etc fields)<br/>
	legacy         (import books from legacy tsv)<br/>
	quit           (quit OLA)<br/>
	help           (print this menu)<br/>
	[Type command to get detailed help]<br/>
</pre></code>



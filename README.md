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
	[ola -b {book file path} -u {users file path}]
</pre></code>

Once OLA starts successfully, the following sub-commands will be available<br/>

<pre><code>
	add		Add new book 	[add -i {input file TSV with new book records}]<br/>
	co		Checkout 	[co -i {book id} -b {borrower id}]<br/>
	ret		return  	[ci -i {book id} -b {borrower_id}]<br/>
	help		detailed help	[help {command}]
</pre></code>



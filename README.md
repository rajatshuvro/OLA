# OLA
Onkur Library Application

The aim of this app is to automate Onkur Bangla Language Academy's library management. Major use cases are:
- Adding/removing new books to the library.
- Book checkout/checkin.
- Maintain a database of book transactions.
- Enable automated queries.

## Command Line Interface

Options:<br/>
<pre><code>	add		Add new book 	[add -i {isbn number} -g {genre} -r {reading level}]</pre></code><br/>
	co		Checkout 		[co -i {book id} -b {borrower id}]<br/>
	ret		return  		[ci -i {book id} -b {borrower_id}]<br/>
	help		detailed help	[help command]

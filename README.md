# OLA
Onkur Library Application

The aim of this app is to automate Onkur Bangla Language Academy's library management. Major use cases are:
- Adding/removing new books to the library.
- Book checkout/checkin.
- Maintain a database of book transactions.
- Enable automated queries.

# Command Line Interface

Options:
	add		Add new book 	[add -i {isbn number} -g {genre} -r {reading level}]
	co		Checkout 		[co -i {book id} -b {borrower id}]
	ret		return  		[ci -i {book id} -b {borrower_id}]
	help	detailed help	[help command]
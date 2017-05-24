#!/usr/bin/python

import os
if __name__ == "__main__":
	index = 1
	while True:
		print os.system("curl -d '%d: Hello Friday!' http://localhost:8080/api/notification/produce/" % index)
		index = index + 1

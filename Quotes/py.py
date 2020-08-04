import json
import codecs


quotes = []

with codecs.open('enquotes', 'r', encoding='utf8') as f:
	text = f.read()
	quotes = text.split('\n')

with codecs.open('enout.json', 'w', encoding='utf8') as f:
	f.write(json.dumps(quotes))
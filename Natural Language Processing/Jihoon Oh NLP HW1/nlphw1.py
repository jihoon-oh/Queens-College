import math

def main():
	uniWordCount = {}
	initializeWordCount("brown-train.txt", uniWordCount, "unigram")
	writeFile("brown-train.txt",  uniWordCount)
	modifyTestFile("brown-test.txt", uniWordCount)
	modifyTestFile("learner-test.txt", uniWordCount)
	uniWordCountSize = totalCountOf(uniWordCount)
	biWordCount = {}
	initializeWordCount("new-brown-train.txt", biWordCount, "bigram")

	# Solution to number 1
	print("PROBLEM 1")
	print("Vocabulary size", vocabularySize(uniWordCount)) 

	# Solution to number 2
	print("PROBLEM 2")
	print("Number of tokens" , uniWordCountSize)
	
	# Solution to number 3
	print("PROBLEM 3")
	print("Percentage of missing words from brown-test.txt",
		percentageMissingUnigram("brown-test.txt", uniWordCount))
	print("Percentage of missing words from learner-test.txt", 
		percentageMissingUnigram("learner-test.txt", uniWordCount))
	
	# Solution to number 4
	print("PROBLEM 4")
	print("Percentage of missing bigram types from brown-test.txt",
		percentageMissingBigram("brown-test.txt", biWordCount))
	print("Percentage of missing bigram types from learner-test.txt", 
		percentageMissingBigram("learner-test.txt", biWordCount))

	# Solution to number 5 and 6
	print("PROBLEM 5 AND 6")
	data = " <s> he was laughed off the screen . </s>"
	print("The following uses this data:" + "\"" + data + "\"")
	print("Unigram:", logUnigramProbability(data, uniWordCount, uniWordCountSize))
	print("Bigram:", logBigramProbability(data, uniWordCount, biWordCount))
	print("Bigram Add One:", logBigramAddOne(data, uniWordCount, biWordCount))
	print("Unigram Perplexity:", perplexity("unigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Perplexity:", perplexity("bigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Add One Perplexity:", perplexity("bigram add one", data, uniWordCount, biWordCount, uniWordCountSize))

	print("PROBLEM 5 AND 6")
	data = "<s> there was no <unk> behind them . </s>"
	print("The following uses this data:" + "\"" + data + "\"")
	print("Unigram:", logUnigramProbability(data, uniWordCount, uniWordCountSize))
	print("Bigram:", logBigramProbability(data, uniWordCount, biWordCount))
	print("Bigram Add One:", logBigramAddOne(data, uniWordCount, biWordCount))
	print("Unigram Perplexity:", perplexity("unigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Perplexity:", perplexity("bigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Add One Perplexity:", perplexity("bigram add one", data, uniWordCount, biWordCount, uniWordCountSize))

	print("PROBLEM 5 AND 6")
	data = "<s> i look forward to hearing your reply . </s>"
	print("The following uses this data:" + "\"" + data + "\"")
	print("Unigram:", logUnigramProbability(data, uniWordCount, uniWordCountSize))
	print("Bigram:", logBigramProbability(data, uniWordCount, biWordCount))
	print("Bigram Add One:", logBigramAddOne(data, uniWordCount, biWordCount))
	print("Unigram Perplexity:", perplexity("unigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Perplexity:", perplexity("bigram", data, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Add One Perplexity:", perplexity("bigram add one", data, uniWordCount, biWordCount, uniWordCountSize))

	# Solution to number 7
	print("PROBLEM 7")
	corpus = ""
	with open("new-brown-test.txt", "r") as file:
		for line in file:
			corpus += line
	print("Unigram for brown test: ",perplexity("unigram", corpus, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram for brown test: ", perplexity("bigram", corpus, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Add One for brown test: ", perplexity("bigram add one", corpus, uniWordCount, biWordCount, uniWordCountSize))

	corpus = ""
	with open("new-learner-test.txt", "r") as file:
		for line in file:
			corpus += line
	print("Unigram for learner test: ", perplexity("unigram", corpus, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram for learner test: ", perplexity("bigram", corpus, uniWordCount, biWordCount, uniWordCountSize))
	print("Bigram Add One for learner test: ", perplexity("bigram add one", corpus, uniWordCount, biWordCount, uniWordCountSize))


# Reads the input file and intializes wordCount dictionary
def initializeWordCount(inputFile, wordCount, whichModel):
	with open(inputFile, "r") as file:
		for line in file:
			line = line.lower()
			if whichModel == "unigram":
				for word in line.split():
					if word not in wordCount:
						wordCount[word] = 1
					else:
						wordCount[word] += 1
			else: 
				lineAsList = line.split()
				for i in range(len(lineAsList) - 1):
					wordTuple = (lineAsList[i], lineAsList[i+1])
					if wordTuple not in wordCount:
						wordCount[wordTuple] = 1
					else:
						wordCount[wordTuple] += 1



# Reads input file again, creates and writes the new tagged sentences to outputFile 
def writeFile(inputFile, wordCount):
	outputFile = open("new-" + inputFile, "w")
	wordCount["<unk>"] = 0
	wordCount["<s>"] = 0
	wordCount["</s>"] = 0
	with open(inputFile, "r") as inFile:
		for line in inFile:
			line = line.lower()
			wordCount["<s>"] += 1
			outputFile.write("<s>")
			for word in line.split():
				if wordCount[word] == 1:
					outputFile.write(" <unk>")
					wordCount["<unk>"] += 1
					del wordCount[word]
				else:
					outputFile.write(" " + word)
			wordCount["</s>"] += 1
			outputFile.write(" </s>\n")
	outputFile.close()

# Pads each sentence with start and stop symbol
# Replaces all words seen in testing not seen in training with <unk>
def modifyTestFile(testFile, wordCount) :
	outputFile = open("new-" + testFile, "w")
	with open(testFile, "r") as inFile:
		for line in inFile:
			line = line.lower()
			outputFile.write("<s>")
			for word in line.split():
				if word not in wordCount:
					outputFile.write(" <unk>")
				else:
					outputFile.write(" " + word)
			outputFile.write(" </s>\n")
	outputFile.close()

# Sole purpose of answering number three
def percentageMissingUnigram(testFile, wordCount):
	countMissing = 0.0
	countTotal = 0.0
	with open(testFile, "r") as inFile:
		for line in inFile:
			line = line.lower()
			for word in line.split():
				countTotal += 1
				if word not in wordCount:
					countMissing += 1
	return countMissing / countTotal

def percentageMissingBigram(testFile, wordCount):
	countMissing = 0.0
	countTotal = 0.0
	with open(testFile, "r") as inFile:
		for line in inFile:
			lineAsList = line.split()
			for i in range(len(lineAsList) - 1):
				countTotal += 1
				wordTuple = (lineAsList[i], lineAsList[i+1])
				if wordTuple not in wordCount:
					countMissing += 1
	return countMissing/countTotal

def totalCountOf(wordCount):
	totalCount = 0.0
	for count in wordCount.values():
		totalCount += count
	return totalCount

def unigramProbability(sentence, wordCount, size):
	prob = 1.0
	for word in sentence.split()[1:]: # [1:] to ignore start symbol
		prob *= (wordCount[word] / size)
	return prob

def bigramProbability(sentence, uniWordCount, biWordCount):
	lineAsList = sentence.split()
	prob = 1.0
	for i in range(len(lineAsList) - 1):
		wordTuple = (lineAsList[i], lineAsList[i+1])
		if wordTuple not in biWordCount:
			return 0
		prob *= (biWordCount[wordTuple] / float(uniWordCount[lineAsList[i]]))
	return prob

def bigramAddOne(sentence, uniWordCount, biWordCount):
	lineAsList = sentence.split()
	prob = 1.0
	v = vocabularySize(uniWordCount)
	for i in range(len(lineAsList) - 1):
		wordTuple = (lineAsList[i], lineAsList[i+1])
		if wordTuple not in biWordCount:
			prob *= (1.0 / (float(uniWordCount[lineAsList[i]]) + v))
		else: 
			prob *= (biWordCount[wordTuple] + 1.0) / (float(uniWordCount[lineAsList[i]]) + v)
	return prob

def logUnigramProbability(sentences, wordCount, size):
	prob = 0.0
	for line in sentences.split("\n"):
		sentenceProb = unigramProbability(line, wordCount, size)
		if sentenceProb == 0: 
			return "undefined"
		else:
			prob += math.log(sentenceProb, 2)
	return prob
	

def logBigramProbability(sentences, uniWordCount, biWordCount):
	prob = 0.0
	for line in sentences.split("\n"):
		sentenceProb = bigramProbability(line, uniWordCount, biWordCount)
		if sentenceProb == 0: 
			return "undefined"
		else: prob += math.log(sentenceProb, 2)
	return prob

def logBigramAddOne(sentences, uniWordCount, biWordCount):
	prob = 0.0
	for line in sentences.split("\n"):
		sentenceProb = bigramAddOne(line, uniWordCount, biWordCount)
		if sentenceProb == 0: 
			return "undefined"
		else: prob += math.log(sentenceProb, 2)
	return prob

def perplexity(model, sentences, uniWordCount, biWordCount, numWords):
	M = 0
	for words in sentences.split():
		M += 1

	if model == "unigram":
		if logUnigramProbability(sentences, uniWordCount, numWords) == "undefined":
			return 1000
		else:
			l = (1.0/M) * logUnigramProbability(sentences, uniWordCount, numWords)
	elif model == "bigram":
		if logBigramProbability(sentences, uniWordCount, biWordCount) == "undefined":
			return 1000
		else:
			l = (1.0/M) * logBigramProbability(sentences, uniWordCount, biWordCount)
	else:
		if logBigramAddOne(sentences, uniWordCount, biWordCount) == "undefined":
			return 1000
		else:
			l = (1.0/M) * logBigramAddOne(sentences, uniWordCount, biWordCount)
	return (2 ** (l * -1))

def vocabularySize(uniWordCount):
	count = 0
	for word in uniWordCount:
		count += 1
	return count

def printWordCount(wordCount):
	for word, count in wordCount.items():
		print(word, count)

main()
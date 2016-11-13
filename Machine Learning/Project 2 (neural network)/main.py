import numpy as np

inputs = []
targets = []
wV1 = []
wv2 = []
bias1 = []
bias2 = []

def initialize(dS, maxAttrs):
    global inputs, targets, wV1, wV2, bias1, bias2
    inputs = []
    targets = []
    # Divide each attribute by the max value for that attribute
    for li in dS:
        li = [float(attr)/maxAttr for attr, maxAttr in zip(li, maxAttrs)]
        inputs.append(li[:-1])
        targets.append([li[-1]])

    np.random.seed(1)
    # Randomly initialize our weights with mean 0
    wV1 = (2.0/7)*np.random.random((7,3)) - (1.0/7)
    wV2 = (2.0/7)*np.random.random((3,1)) - (1.0/7)

    # Randomly initialize bias weights with mean 0
    bias1 = (2.0/7)*np.random.random((209, 3)) - (1.0/7)
    bias2 = (2.0/7)*np.random.random((209,1)) - (1.0/7)

def sigmoid(x,deriv=False):
    if(deriv==True):
        return x*(1-x)
    return 1/(1+np.exp(-x))

def train(lower, upper, output): 
    if lower >= upper: return

    global inputs, targets, wV1, wV2, bias1, bias2
    # Convert list to array
    inputsArr = np.asarray(inputs[lower:upper])
    targetsArr = np.asarray(targets[lower:upper])

    error = float("inf")
    threshold = 0.1
    i = 0   
    while error > threshold:
        # Forward Propogation
        ai = inputsArr
        aj = sigmoid(np.dot(ai,wV1) + bias1[lower:upper]) # Nodes in hidden layer
        ak = sigmoid(np.dot(aj,wV2) + bias2[lower:upper]) # Target node

        # Backward Propagation
        delta_k = (targetsArr - ak) * sigmoid(ak,deriv=True)
        delta_j = delta_k.dot(wV2.T) * sigmoid(aj,deriv=True)

        # Update weights
        wV2 += aj.T.dot(delta_k)
        wV1 += ai.T.dot(delta_j)

        # Update biases
        bias1[lower:upper] += delta_j
        bias2[lower:upper] += delta_k

        error = np.sum((targetsArr - ak) ** 2) / 2  
        output.write("Epoch " + str(i) + ": " + str(error) + "\n")
        i += 1

def test(lower, upper, output): 
    # Safety conditional
    if lower >= upper: return

    # Convert list to array
    global inputs, targets
    inputsArr = np.asarray(inputs[lower:upper])
    targetsArr = np.asarray(targets[lower:upper])

    # Forward Propogation
    ai = inputsArr
    aj = sigmoid(np.dot(ai,wV1) + bias1[lower:upper]) # Nodes in hidden layer
    ak = sigmoid(np.dot(aj,wV2) + bias2[lower:upper]) # Target node

    error = np.sum((targetsArr - ak) ** 2) / 2 
    output.write("----------------------------\n")
    output.write("TESTING ERROR: " + str(error) + "\n")
    output.write("----------------------------\n")
    
# Read file, initialize dataSet
dS = [[0 for y in range(8)] for x in range(209)] # dS = dataSet
maxAttrs = [0] * 8 # Max value for each attr is set to 0
with open("data.txt", "r") as file:
    r = 0
    for line in file:
        c = 0
        for attr in line.split(",")[2:]:
            dS[r][c] = int(attr)
            maxAttrs[c] = max(maxAttrs[c], dS[r][c])
            c += 1
        r += 1

output = open("output.txt", "w")
for fold in xrange(5):
    output.write("======= Fold " + str(fold) + " =======\n")
    initialize(dS, maxAttrs)
    train(0, fold*42, output)
    test(fold*42, (fold*42)+42, output)
    train((fold*42)+42, len(inputs), output)
    output.write("\n")
output.close()
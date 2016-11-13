import numpy as np

inputs = []
targets = []
wV1 = []
wv2 = []
bias1 = []
bias2 = []

def initialize(dS, maxAttrs):
    global inputs, targets, wV1, wV2, bias1, bias2
    # Divide each attribute by the max value for that attribute
    for li in dS:
        li = [float(attr)/maxAttr for attr, maxAttr in zip(li, maxAttrs)]
        inputs.append(li[:-1])
        targets.append([li[-1]])

    # Randomly initialize our weights with mean 0
    wV1 = (2.0/7)*np.random.random((7,3)) - (1.0/7)
    wV2 = (2.0/7)*np.random.random((3,1)) - (1.0/7)

    # Randomly initialize bias weights with mean 0
    bias1 = (2.0/7)*np.random.random((len(inputs),3)) - (1.0/7)
    bias2 = (2.0/7)*np.random.random((len(inputs),1)) - (1.0/7)

def sigmoid(x,deriv=False):
    if(deriv==True):
        return x*(1-x)
    return 1/(1+np.exp(-x))

def train(lower, upper): 
    if lower >= upper: return

    global inputs, targets, wV1, wV2, bias1, bias2
    # Convert list to array
    inputs = np.asarray(inputs[lower:upper])
    targets = np.asarray(targets[lower:upper])

    error = float("inf")
    threshold = 0.001
    i = 0
    while error > threshold:
        # Forward Propogation
        ai = inputs
        aj = sigmoid(np.dot(ai,wV1) + bias1) # Nodes in hidden layer
        ak = sigmoid(np.dot(aj,wV2) + bias2) # Target node

        # Backward Propagation
        delta_k = (targets - ak) * sigmoid(ak,deriv=True)
        delta_j = delta_k.dot(wV2.T) * sigmoid(aj,deriv=True)

        # Update weights
        wV2 += aj.T.dot(delta_k)
        wV1 += ai.T.dot(delta_j)

        # Update biases
        bias1 += delta_j
        bias2 += delta_k

        error = np.sum((targets - ak) ** 2) / 2  
        print i
        print error
        i += 1

def test(lower, upper): 
    # Safety conditional
    if lower >= upper: return

    # Convert list to array
    global inputs, targets
    inputs = np.asarray(inputs[lower:upper])
    targets = np.asarray(targets[lower:upper])

    # Forward Propogation
    ai = inputs
    aj = sigmoid(np.dot(ai,wV1) + bias1) # Nodes in hidden layer
    ak = sigmoid(np.dot(aj,wV2) + bias2) # Target node

    error = np.sum((targets - ak) ** 2) / 2 
    print "ERROR"
    print error
    
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

for j in xrange(5):
    initialize(dS, maxAttrs)
    train(0, j*42)
    test(j*42, (j*42)+42)
    train((j*42)+42, len(inputs))